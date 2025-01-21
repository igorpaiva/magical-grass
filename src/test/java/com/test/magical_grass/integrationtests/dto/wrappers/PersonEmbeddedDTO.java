package com.test.magical_grass.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.magical_grass.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbeddedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("personDTOList")
    private List<PersonDTO> personDTOList;

    public PersonEmbeddedDTO() {}

    public List<PersonDTO> getPersonDTOList() {
        return personDTOList;
    }

    public void setPersonDTOList(List<PersonDTO> personDTOList) {
        this.personDTOList = personDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonEmbeddedDTO that = (PersonEmbeddedDTO) o;
        return Objects.equals(personDTOList, that.personDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(personDTOList);
    }
}
