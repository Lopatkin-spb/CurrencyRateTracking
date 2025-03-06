package com.example.currencyratetracking.common

import javax.inject.Scope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class ApplicationScope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class ActivityScope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class FragmentScope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class ServiceScope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class CoroutineScope


@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
public annotation class NetworkScope