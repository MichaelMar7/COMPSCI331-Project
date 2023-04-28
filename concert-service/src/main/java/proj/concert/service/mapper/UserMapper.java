package proj.concert.service.mapper;

import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

public class UserMapper {

    public static User toDomainModel(UserDTO userDTO) {
        return new User(
                userDTO.getUsername(),
                userDTO.getPassword()
        );
    }

    public static UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO(
                user.getUsername(),
                user.getPassword()
        );
        return userDTO;
    }
}

