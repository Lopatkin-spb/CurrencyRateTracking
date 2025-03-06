package com.example.currencyratetracking.api_remote.di

import javax.inject.Qualifier


@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
public annotation class ApiUrl

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
public annotation class BuildType