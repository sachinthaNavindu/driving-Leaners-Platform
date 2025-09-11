    package lk.ijse.drivingschool.repository;

    import lk.ijse.drivingschool.entity.Employee;
    import lk.ijse.drivingschool.entity.Instructor;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;
    import java.util.Optional;

    public interface EmployeeRepo extends JpaRepository<Employee, String> {
        Optional<Employee> findByNicAndGmail(String nic, String email);
        Optional<Employee> findByNic(String nic);

        @Query("SELECT e FROM Employee e WHERE e.jobRole='Instructor'")
        List<Employee> findAllInstructorEmployees();

        @Query("SELECT e.name, i.nic FROM Instructor i JOIN Employee e ON i.nic = e.nic WHERE i.status = 'ACTIVE'")
        List<Object[]> findAllInstructorsByStatus();

        @Query("SELECT i.employee.gmail FROM Instructor i WHERE i.nic = :nic")
        String findGmailByLicenseId(@Param("nic") String nic);
    }
