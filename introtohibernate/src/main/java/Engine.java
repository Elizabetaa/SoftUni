import com.sun.javaws.IconUtil;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Write which exercise want to check: ");
        int ex = 0;

        try {
            ex = Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("Invalid exercise!");
            System.out.println("Input must be single integer between 1 and 13!");
            return;
        }

        switch (ex) {
            case 1:
                System.out.println("The database is created!");
                break;
            case 2:
                changeCasingEx2();
                break;
            case 3:
                try {
                    containsEmployeeEx3();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                employeeSalaryOver50000Ex4();
                break;
            case 5:
                employeesFromDepartmentEx5();
                break;
            case 6:
                try {
                    addingNewAddressAndUpdatingEmployeeEx6();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                addressWithEmployeeCountEx7();
                break;
            case 8:
                try {
                    getEmployeeWithProjectEx8();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                findLatest10ProjectsEx9();
                break;
            case 10:
                increaseSalariesEx10();
                break;
            case 11:
                try {
                    findEmployeesByFirstNameEx11();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                employeesMaximumSalaries12();
                break;
            case 13:
                try {
                    removeTownsEx13();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid exercise!");
                System.out.println("Input must be single integer between 1 and 13!");
                break;
        }


    }

    private void removeTownsEx13() throws IOException {
        System.out.println("Write town to be deleted:");
        String townName = reader.readLine();
        Town town = entityManager.createQuery("SELECT t FROM Town t " +
                "WHERE t.name = :name", Town.class)
                .setParameter("name", townName)
                .getSingleResult();
        entityManager.getTransaction().begin();

        entityManager
                .createQuery("UPDATE Employee e " +
                        "SET e.address.id = null " +
                        "WHERE e.address in (SELECT a FROM Address a " +
                        "WHERE a.town.id = :townId)")
                .setParameter("townId", town.getId())
                .executeUpdate();

        int countDeleted =
                entityManager.createQuery("DELETE FROM Address a WHERE a.town.id = :townId")
                        .setParameter("townId", town.getId())
                        .executeUpdate();

        entityManager.createQuery("DELETE FROM Town t WHERE t.name  = :townName")
                .setParameter("townName", townName).executeUpdate();

        entityManager.getTransaction().commit();

        System.out.printf("%d addresses in %s deleted", countDeleted, townName);
    }

    private void employeesMaximumSalaries12() {
        StringBuilder employees = new StringBuilder();
        entityManager
                .createQuery("SELECT e FROM  Employee AS e " +
                        "WHERE e.salary NOT BETWEEN 30000 AND 70000 " +
                        "GROUP BY e.department " +
                        "ORDER BY e.salary DESC", Employee.class)
                .getResultList()
                .stream()
                .sorted(Comparator.comparing(e -> e.getDepartment().getId()))
                .forEach(employee -> employees.append(String.format("%s - %.2f%n",
                        employee.getDepartment().getName(), employee.getSalary())));
        System.out.println(employees.toString().trim());
    }

    private void findEmployeesByFirstNameEx11() throws IOException {
        System.out.println("Write pattern: ");
        String pattern = reader.readLine();
        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.firstName like concat(:pattern,'%') ", Employee.class)
                .setParameter("pattern", pattern)
                .getResultList();
        employees.forEach(e -> {
            System.out.printf("%s %s - %s - ($%.2f)%n", e.getFirstName(), e.getLastName(),
                    e.getJobTitle(), e.getSalary());
        });

    }

    private void increaseSalariesEx10() {
        entityManager.getTransaction().begin();
        entityManager
                .createQuery("UPDATE Employee e " +
                        "SET e.salary = e.salary * 1.12 " +
                        "WHERE e.department.id IN (1,2,4,11)")
                .executeUpdate();
        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM  Employee  e " +
                        "WHERE e.department.id IN (1,2,4,11)", Employee.class)
                .getResultList();

        employees.forEach(e -> {
            System.out.printf("%s %s - ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary());
        });
    }

    private void findLatest10ProjectsEx9() {
        List<Project> projects = entityManager
                .createQuery("SELECT p FROM Project p " +
                        "order by p.startDate DESC ", Project.class)
                .setMaxResults(10)
                .getResultList();

        projects
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    System.out.printf("Project name: %s%n" +
                                    " \tProject Description: %s%n" +
                                    " \tProject Start Date:%s%n" +
                                    " \tProject End Date: %s%n",
                            p.getName(),
                            p.getDescription(),
                            p.getStartDate(),
                            p.getEndDate());
                });
        System.out.println();
    }

    private void getEmployeeWithProjectEx8() throws IOException {
        System.out.println("Write valid employee id:");
        int id = Integer.parseInt(reader.readLine());

        Employee employee = entityManager.find(Employee.class, id);

        System.out.printf("%s %s - %s%n", employee.getFirstName(),
                employee.getLastName(), employee.getJobTitle());

        employee
                .getProjects()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    System.out.printf(" %s%n", p.getName());
                });
    }

    private void addressWithEmployeeCountEx7() {
        List<Address> addresses = entityManager
                .createQuery("select a from Address a " +
                        "order by a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses
                .forEach(address -> {
                    System.out.printf("%s, %s - %d employees %n",
                            address.getText(), address.getTown().getName(),
                            address.getEmployees().size());
                });

    }

    private void addingNewAddressAndUpdatingEmployeeEx6() throws IOException {
        Address address = createAddress("Vitoshka 15");

        System.out.println("Enter employee last name:");
        String lastName = reader.readLine();

        Employee employee = (Employee) entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.lastName = :name")
                .setParameter("name", lastName)
                .getSingleResult();

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();

        System.out.printf("Successfully change employee address with last name %s on Vithoshka 15%n",
                employee.getLastName());
    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void employeesFromDepartmentEx5() {
        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = 'Research and Development' " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .getResultList();
        for (Employee employee : employees) {
            System.out.printf("%s %s from %s - $%.2f%n", employee.getFirstName(),
                    employee.getLastName(),
                    employee.getDepartment().getName(),
                    employee.getSalary());
        }
    }

    private void employeeSalaryOver50000Ex4() {
        entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.salary > 50000", Employee.class)
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void containsEmployeeEx3() throws IOException {
        System.out.println("Write employee full name who you want to check whether exist:");
        String employeeName = reader.readLine();

        List<Employee> employees = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE concat(e.firstName,' ', e.lastName) = :name", Employee.class)
                .setParameter("name", employeeName)
                .getResultList();

        System.out.println(employees.size() > 0 ? "Yes" : "No");
    }

    private void changeCasingEx2() {
        List<Town> towns = entityManager
                .createQuery("SELECT t FROM Town t " +
                        "WHERE length(t.name) <= 5 ", Town.class)
                .getResultList();

        entityManager.getTransaction().begin();
        towns.forEach(entityManager::detach);
        for (Town town : towns) {
            town.setName(town.getName().toLowerCase());
        }
        towns.forEach(entityManager::merge);
        entityManager.flush();
        entityManager.getTransaction().commit();
        System.out.println("The names of towns with more than 5 symbols was transform to lower case!");
    }

}
