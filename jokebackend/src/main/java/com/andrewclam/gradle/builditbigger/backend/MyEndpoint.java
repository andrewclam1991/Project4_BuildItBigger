/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.andrewclam.gradle.builditbigger.backend;

import com.andrewclam.Joker;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.andrewclam.com",
                ownerName = "backend.builditbigger.gradle.andrewclam.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    // Commented out example sayHi function
//    /**
//     * A simple endpoint method that takes a name and says Hi back
//     */
//    @ApiMethod(name = "sayHi")
//    public MyJoker sayHi(@Named("name") String name) {
//        MyJoker response = new MyJoker();
//        response.setData("Hi, " + name);
//
//        return response;
//    }

    @ApiMethod(name = "getJoke")
    public MyJoker getJoke()
    {
        // TODO [Fetch Joke] - Retrieve the joke from the java library dependency
        Joker joker = new Joker();

        MyJoker response = new MyJoker();
        response.setData(joker.getJoke());
        return response;
    }

}
