package com.kiolis.optimizer.menu.data;

import com.kiolis.optimizer.menu.models.Substance;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

/**
 * Created by kiolis on 26/05/15.
 */
public interface RecipeFetcher {
  void fetch(List<Substance> substances) throws UnirestException;
}
