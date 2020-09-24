package main

import (
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis/v8"
	"github.com/gorilla/mux"
	"io/ioutil"
	"net/http"
)

var redisDBAutoRank *redis.Client

const permAutoRank = "autorank"

func init() {
	redisDBAutoRank = newClient(redisDatabaseAutoRank)
}

func GetAutoRank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	name := vars["name"]
	if redisDBAutoRank.Exists(ctx, name).Val() == 1 {
		w.Header().Set("content-type", "application/json")
		w.Header().Set("version", version)
		w.Write([]byte(redisDBAutoRank.Get(ctx, name).Val()))
	} else {
		w.WriteHeader(http.StatusNotFound)
	}
}

func SetAutoRank(w http.ResponseWriter, r *http.Request) {
	if !hasPermission(GetPermission(r.Header.Get("token")), permAutoRank) {
		http.Error(w, "Forbidden", http.StatusForbidden)
		return
	}
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var rank AutoRank
	err = json.Unmarshal(b, &rank)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	output, err := json.Marshal(rank)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	redisDBAutoRank.Set(ctx, rank.Rank, output, 0)
	w.WriteHeader(http.StatusCreated)
}

func DelAutoRank(w http.ResponseWriter, r *http.Request) {
	if !hasPermission(GetPermission(r.Header.Get("token")), permAutoRank) {
		http.Error(w, "Forbidden", http.StatusForbidden)
		return
	}
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var rank AutoRank
	err = json.Unmarshal(b, &rank)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	redisDBAutoRank.Del(ctx, rank.Rank)
	w.WriteHeader(http.StatusCreated)
}

func GetAllAutoRanks(w http.ResponseWriter, _ *http.Request) {
	var data []AutoRank
	for entry := range redisDBAutoRank.Keys(ctx, "*").Val() {
		var rank AutoRank
		json.Unmarshal([]byte(redisDBAutoRank.Get(ctx, redisDBAutoRank.Keys(ctx, "*").Val()[entry]).Val()), &rank)
		data = append(data, rank)
	}
	output, err := json.MarshalIndent(data, " ", " ")
	if err != nil {
		fmt.Fprintln(w, "{}")
		return
	}
	fmt.Fprintln(w, string(output))
}