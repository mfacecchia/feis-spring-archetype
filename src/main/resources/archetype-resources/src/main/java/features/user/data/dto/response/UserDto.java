package ${package}.features.user.data.dto.response;

import ${package}.common.data.dto.BaseGetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseGetDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}
