import frontcontroller.FrontController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import jdk.nashorn.internal.lookup.Lookup;
import util.ConnectionUtil;
import util.LookupTables;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        ConnectionUtil.getConnectionParams();
        LookupTables.getLookupTables();
        Javalin javalin = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/frontend", Location.CLASSPATH);
        }).start(9000);
        new FrontController(javalin);
    }
}
