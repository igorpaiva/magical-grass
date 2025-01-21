package com.test.magical_grass.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PersonDTOWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO personEmbeddedDTO;

    public PersonEmbeddedDTO getEmbedded() {
        return personEmbeddedDTO;
    }

    public void setEmbedded(PersonEmbeddedDTO personEmbeddedDTO) {
        this.personEmbeddedDTO = personEmbeddedDTO;
    }

}
