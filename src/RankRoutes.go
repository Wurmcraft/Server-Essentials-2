package main

import (
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis/v8"
	"github.com/gorilla/mux"
	"io/ioutil"
	"net/http"
)

var redisDBRank *redis.Client

const permRank = "rank"

func init() {
	redisDBRank = newClient(redisDatabaseRank)
}

func GetRank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	name := vars["uuid"]
	if redisDBRank.Exists(ctx, name).Val() == 1 {
		w.Header().Set("content-type", "application/json")
		w.Header().Set("version", version)
		w.Write([]byte(redisDBRank.Get(ctx, name).Val()))
	} else {
		w.WriteHeader(http.StatusNotFound)
	}
}

func SetRank(w http.ResponseWriter, r *http.Request) {
	if !hasPermission(GetPermission(r.Header.Get("token")), permRank) {
		http.Error(w, "Forbidden", http.StatusForbidden)
		return
	}
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var rank Rank
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
	redisDBRank.Set(ctx, rank.Name, output, 0)
	w.WriteHeader(http.StatusCreated)
}

func DelRank(w http.ResponseWriter, r *http.Request) {
	if !hasPermission(GetPermission(r.Header.Get("token")), permRank) {
		http.Error(w, "Forbidden", http.StatusForbidden)
		return
	}
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var rank Rank
	err = json.Unmarshal(b, &rank)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	redisDBRank.Del(ctx, rank.Name)
	w.WriteHeader(http.StatusOK)
}

func GetAllRanks(w http.ResponseWriter, _ *http.Request) {
	var data []Rank
	for entry := range redisDBRank.Keys(ctx, "*").Val() {
		var rank Rank
		json.Unmarshal([]byte(redisDBRank.Get(ctx, redisDBRank.Keys(ctx, "*").Val()[entry]).Val()), &rank)
		data = append(data, rank)
	}
	output, err := json.MarshalIndent(data, " ", " ")
	if err != nil {
		fmt.Fprintln(w, "{}")
		return
	}
	fmt.Fprintln(w, string(output))
}