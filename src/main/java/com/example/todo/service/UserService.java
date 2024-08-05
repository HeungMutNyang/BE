//package com.example.todo.service;
//
//import com.example.todo.model.UserEntity;
//import com.example.todo.persistence.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    public UserEntity create(final UserEntity userEntity) {
//        if(userEntity == null || userEntity.getEmail() == null) {
//            throw new RuntimeException("Invalid arguments");
//        }
//        final String email = userEntity.getEmail();
//        if(userRepository.existsByEmail(email)){
//            log.warn("Email already exists {}", email);
//            throw new RuntimeException("Email already exists");
//        }
//
//        return userRepository.save(userEntity);
//    }
//
//    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
//        final UserEntity originalUser = userRepository.findByEmail(email);
//
//        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
//            return originalUser;
//        }
//        return null;
//    }
//    //추가
//  	public UserEntity getById(String userId) {
//          return userRepository.findById(userId).orElse(null);
//      }
//
//
//  	public void delete(String userId) {
//          if (userRepository.existsById(userId)) {
//              userRepository.deleteById(userId);
//          } else {
//              throw new RuntimeException("[account_delete]id does not exist");
//          }
//      }
//  	//추가
//}

package com.example.todo.service;

import com.example.todo.dto.UserUpdateDto;
import com.example.todo.model.UserEntity;
import com.example.todo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)){
            log.warn("Email already exists {}", email);
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        if(originalUser != null) {
            if (encoder.matches(password, originalUser.getPassword())) {
                return originalUser;
            } else {
                throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 계정입니다.");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserEntity getById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("[account_delete]id does not exist");
        }
    }

    public UserEntity updateUser(UserUpdateDto userUpdateDto) {
        UserEntity user = userRepository.findById(userUpdateDto.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userUpdateDto.getUsername() != null) {
            user.setUsername(userUpdateDto.getUsername());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getHeight() != null) {
            user.setHeight(userUpdateDto.getHeight());
        }
        if (userUpdateDto.getWeight() != null) {
            user.setWeight(userUpdateDto.getWeight());
        }

        return userRepository.save(user);
    }
}

