package org.lanahub.lanahub.repository;

import org.lanahub.lanahub.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
