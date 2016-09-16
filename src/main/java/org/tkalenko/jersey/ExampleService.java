package org.tkalenko.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/example")
public class ExampleService {

    @GET
    public Response getMessage(){
        String res = "Hello user from app!";
        return Response.status(200).entity(res).build();
    }
}
