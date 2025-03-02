package handler;

import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    public Object clear(Request request, Response response)
    {
        ClearService.clear();
        return "null";
    }
}
