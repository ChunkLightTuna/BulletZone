package com.cs619.alpha.tankclient.rest;

import com.cs619.alpha.tankclient.util.BooleanWrapper;
import com.cs619.alpha.tankclient.util.GridWrapper;
import com.cs619.alpha.tankclient.util.LongWrapper;

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
 * Created by alphajin on 10/21/15.
 */

@Rest(rootUrl = "http://stman1.cs.unh.edu:6192/games",
        converters = {StringHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class}
)
/**
 * Rest client for retrieving from shitty central server. Beware of exceptions!
 */
public interface BulletZoneRestClient extends RestClientErrorHandling {
  void setRootUrl(String rootUrl);

  /**
   * Join Server.
   *
   * @return LongWrapper
   * @throws RestClientException
   */
  @Post("")
  LongWrapper join() throws RestClientException;


  /**
   * Get game state.
   *
   * @return GridWrapper
   */
  @Get("")
  GridWrapper grid();

  /**
   * Update tank position
   *
   * @param tankId    long
   * @param direction byte
   * @return BooleanWrapper
   */
  @Put("/{tankId}/move/{direction}")
  BooleanWrapper move(long tankId, byte direction);

  /**
   * turn tank (l/r).
   *
   * @param tankId    long
   * @param direction byte
   * @return BooleanWrapper
   */
  @Put("/{tankId}/turn/{direction}")
  BooleanWrapper turn(long tankId, byte direction);

  /**
   * Pew pew pew.
   *
   * @param tankId long
   * @return BooleanWrapper
   */
  @Put("/{tankId}/fire/{strength}")
  BooleanWrapper fire(long tankId, int strength);

  /**
   * Quit game. reported as buggy. beware!
   *
   * @param tankId long
   * @return BooleanWrapper
   */
  @Delete("/{tankId}/leave")
  String leave(long tankId);
}