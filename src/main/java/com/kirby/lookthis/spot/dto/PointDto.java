package com.kirby.lookthis.spot.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PointDto {
    public Integer point;
    public String userId;
    public Integer flyerId;
    public Integer spotId;
    public Integer flyerSpotId;

}
