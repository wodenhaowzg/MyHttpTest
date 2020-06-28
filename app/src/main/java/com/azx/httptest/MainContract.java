package com.azx.httptest;

interface MainContract {

    interface View {

        void showNews(String news);
    }

    interface Presenter  {

        boolean requestAsyncNews();
    }
}
