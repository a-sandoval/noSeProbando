package server;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerProperties;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import server.handlers.AppHandlers;
import server.middlewares.AuthMiddleware;
import server.middlewares.SessionMiddleware;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class Server {
    
    private static Javalin app=null;
    
    public static Javalin app(){
        if(app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }
    
    public static void init(){
        if(app==null){

            String strport = System.getenv("PORT");
            if (strport == null){
                strport = "8089";
            }
            Integer port = Integer.parseInt(strport);

            app = Javalin.create(config()).start(port);
            initTemplateEngine();
            AppHandlers.applyHandlers(app);
            Router.init();
            //Server.configureEntityManagerProperties();
            //Initializer.init();
        }
    }

    private static Consumer<JavalinConfig> config() {
        return config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/assets";
            });
            AuthMiddleware.apply(config);
            SessionMiddleware.apply(config);
        };
    }

    private static  void initTemplateEngine() {
        JavalinRenderer.register(
                (path, model, context) -> { // Función que renderiza el template
                    Handlebars handlebars = new Handlebars();
                    handlebars.registerHelper("formateo", new Helper<LocalDateTime>() {
                        @Override
                        public CharSequence apply(LocalDateTime fecha, Options options) {
                            if (fecha == null) return "";

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                            return fecha.format(formatter);
                        }
                    });
                    Template template = null;
                    try {
                        template = handlebars.compile(
                                "templates/" + path.replace(".hbs",""));
                        return template.apply(model);
                    } catch (IOException e) {
                        e.printStackTrace();
                        context.status(HttpStatus.NOT_FOUND);
                        return "No se encuentra la página indicada...";
                    }
                }, ".hbs" // Extensión del archivo de template
        );
    }
    /*public static void configureEntityManagerProperties() {

        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<>();

        String[] keys = new String[] {
                "hibernate.archive.autodetection",
                "hibernate.connection.driver_class",
                "hibernate.connection.url",
                "hibernate.connection.username",
                "hibernate.connection.password",
                "hibernate.dialect",
                "hibernate.show_sql",
                "hibernate.format_sql",
                "use_sql_comments",
                "hibernate.hbm2ddl.auto",
                };

        for (String key : keys) {
            if (env.containsKey(key)) {
                String value = env.get(key);
                System.out.println(key + ": " + value);
                configOverrides.put(key, value);
            }
        }
        Consumer<PerThreadEntityManagerProperties> propertiesConsumer = perThreadEntityManagerProperties -> {
            perThreadEntityManagerProperties.putAll(configOverrides);
        };

        WithSimplePersistenceUnit.configure(propertiesConsumer);

*/
}

