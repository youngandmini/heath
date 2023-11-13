package heavysnow.heath.dto.post;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDatesResponseDto {

    private List<LocalDate> postDates;

    public static PostDatesResponseDto of(List<LocalDateTime> postDatetimes) {

        return new PostDatesResponseDto(postDatetimes.stream().map(LocalDate::from).collect(Collectors.toList()));
    }
}
