package upe.test;

import upe.annotations.UpeBackendFacade;

@UpeBackendFacade("stringProvider")
public interface BackendService {
    String NAME = "stringProvider";

    String get();
}
