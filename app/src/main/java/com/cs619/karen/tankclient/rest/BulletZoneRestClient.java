package com.cs619.karen.tankclient.rest;

import com.cs619.karen.tankclient.util.BooleanWrapper;
import com.cs619.karen.tankclient.util.GridWrapper;
import com.cs619.karen.tankclient.util.LongWrapper;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;

/**
 * Created by karenjin on 10/21/15.
 */

@Rest(rootUrl = "http://stman1.cs.unh.edu:6191/games",
        converters = {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class}
)
public interface BulletZoneRestClient extends RestClientErrorHandling {
    void setRootUrl(String rootUrl);

    @Post("")
    LongWrapper join() throws RestClientException;

    @Get("")
    GridWrapper grid();

    @Put("/{tankId}/move/{direction}")
    BooleanWrapper move(long tankId, byte direction);

    @Put("/{tankId}/turn/{direction}")
    BooleanWrapper turn(long tankId, byte direction);

    @Put("/{tankId}/fire")
    BooleanWrapper fire(long tankId);

    @Delete("/{tankId}/leave")
    BooleanWrapper leave(long tankId);
}