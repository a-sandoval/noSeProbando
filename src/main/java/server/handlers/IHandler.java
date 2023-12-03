package server.handlers;

import io.javalin.Javalin;

public interface IHandler {
    void setHandle(Javalin app);
}
