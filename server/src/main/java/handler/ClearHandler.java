package handler;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.ClearService;

import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    private final ClearService service;

    public ClearHandler(UserDAO userDb, AuthDAO authDb, GameDAO gameDb) {
        this.service = new ClearService(userDb, authDb, gameDb);
    }

    public Object clear(Request request, Response response)
    {
        service.runClear();
        return "null";
    }
}
