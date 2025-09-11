    package lk.ijse.drivingschool.entity;

    import jakarta.persistence.Entity;
    import jakarta.persistence.EnumType;
    import jakarta.persistence.Enumerated;
    import jakarta.persistence.Id;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class Employee {

        @Id
        private String nic;

        private String name;
        private String gmail;
        private String address;
        private String contact;
        @Enumerated(EnumType.STRING)
        private JobRole jobRole;
    }
