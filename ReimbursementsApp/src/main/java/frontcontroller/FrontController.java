package frontcontroller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.javalin.Javalin;
import models.JsonResponse;

public class FrontController {
    public FrontController(Javalin javalin) {
        javalin.exception(NumberFormatException.class, (e, context) -> {
            context.status(400);
            context.json(new JsonResponse(false, 400, "Certain URI or query parameter values need to be numeric, non-numeric value detected.", null));
        });
        javalin.exception(UnrecognizedPropertyException.class, (e, context) -> {
            context.status(400);
            context.json(new JsonResponse(false, 400, "JSON field error check names and number of fields(keys) and formatting.", null));
        });
        javalin.exception(JsonParseException.class, (e, context) -> {
            context.status(400);
            context.json(new JsonResponse(true, 400, "JSON formatting error check formatting of JSON object in body.", null));
        });
        new Dispatcher(javalin);
    }
}
