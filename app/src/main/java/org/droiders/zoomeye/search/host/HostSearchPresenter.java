package org.droiders.zoomeye.search.host;

import javax.inject.Inject;
import org.droiders.zoomeye.search.SearchFragmentContract;
import org.zoomeye.api.ErrorBody;
import org.zoomeye.api.ErrorBodyHandler;
import org.zoomeye.api.ZoomEyeApiService;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Donglua on 16/4/29.
 */
public class HostSearchPresenter implements SearchFragmentContract.Presenter {

  private final ZoomEyeApiService api;
  private final SearchFragmentContract.View view;
  private final ErrorBodyHandler errorBodyHandler;

  @Inject
  public HostSearchPresenter(ZoomEyeApiService api,
      SearchFragmentContract.View view, ErrorBodyHandler errorBodyHandler) {
    this.api = api;
    this.view = view;
    this.errorBodyHandler = errorBodyHandler;
  }

  @Override public void search(String query, int page) {
    api.searchHost(query, page, "")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(searchResultResponse -> {

          if (searchResultResponse.isSuccessful()) {

            view.showHostMatches(searchResultResponse.body().getMatches());

          } else {
            ErrorBody errorBody = errorBodyHandler.parseError(searchResultResponse.errorBody());
            view.showErrorMessage(errorBody.getMessage());
          }

        });
  }
}
