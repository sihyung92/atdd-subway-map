package wooteco.subway.controller.web.station;

import wooteco.subway.station.domain.Station;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName().text());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}