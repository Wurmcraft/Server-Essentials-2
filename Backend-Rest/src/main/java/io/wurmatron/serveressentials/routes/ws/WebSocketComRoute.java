package io.wurmatron.serveressentials.routes.ws;

import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsHandler;
import io.wurmatron.serveressentials.models.AuthUser;
import io.wurmatron.serveressentials.models.DataWrapper;
import io.wurmatron.serveressentials.models.MessageResponse;
import io.wurmatron.serveressentials.models.WSWrapper;
import io.wurmatron.serveressentials.routes.EndpointSecurity;
import io.wurmatron.serveressentials.routes.Route;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.function.Consumer;

import static io.wurmatron.serveressentials.ServerEssentialsRest.GSON;
import static io.wurmatron.serveressentials.ServerEssentialsRest.LOG;
import static io.wurmatron.serveressentials.routes.RouteUtils.response;

public class WebSocketComRoute {

    public static NonBlockingHashMap<WsContext, String> activeConnections = new NonBlockingHashMap<>();

    @Route(path = "api/live", method = "WS")
    public static Consumer<WsHandler> ws = ws -> {

        ws.onConnect(ctx -> {
            String token = ctx.cookie("authentication");
            if (!activeConnections.containsKey(token)) {
                if (EndpointSecurity.authTokens.containsKey(token)) {
                    AuthUser serverPerms = EndpointSecurity.authTokens.get(token);
                    if (serverPerms.type.equalsIgnoreCase("SERVER")) {
                        activeConnections.put(ctx, serverPerms.name);
                        ctx.send(GSON.toJson(new WSWrapper(200, WSWrapper.Type.UPDATE, new DataWrapper(AuthUser.class.getTypeName(), GSON.toJson(serverPerms)))));
                        LOG.info(activeConnections.get(ctx) + " has connected to the Web Socket");
                    } else
                        ctx.send(GSON.toJson(new WSWrapper(409, WSWrapper.Type.MESSAGE, new DataWrapper(MessageResponse.class.getTypeName(), response("Invalid Type", "Only servers can access the live data stream")))));
                }
            }
        });

        ws.onMessage(ctx -> {
            if (activeConnections.containsKey(ctx)) {

            } else
                ctx.send(GSON.toJson(new WSWrapper(400, WSWrapper.Type.MESSAGE, new DataWrapper(MessageResponse.class.getTypeName(), response("No Auth", "Failed to authenticate")))));
        });

        ws.onClose(ctx -> {
            if (activeConnections.containsKey(ctx)) {
                LOG.info(activeConnections.get(ctx) + " has disconnected from the Web Socket");
                activeConnections.remove(ctx);
            }
        });

        ws.onError(ctx -> {

        });
    };
}
