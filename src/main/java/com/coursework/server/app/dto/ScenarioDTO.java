package com.coursework.server.app.dto;

public class ScenarioDTO {
    private Integer id;
    private String name; // Добавь необходимые поля сценария
    private String companyName;

    public ScenarioDTO() {
    }

    public ScenarioDTO(Integer id, String name, String companyName) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
