
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// The Java class will be hosted at the URI path "/helloworld"
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/catalog")
public class CatalogResource {

    @GET
    public Response getClichedMessage() {
        // Return some cliched textual content
        return Response.ok("hello").build();
    }
}
