import com.sun.javaws.IconUtil;
import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
//        Ex2
//         changeCasingEx2();
//
//        Ex3
//        try {
//            containsEmployeeEx3();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Ex4
//        employeeSalaryOver50000Ex4();
//
//        Ex5
//        employeesFromDepartmentEx5();

//          Ex6
//        try {
//            addingNewAddressAndUpdatingEmployeeEx6();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Ex7
        addressWithEmployeeCountEx6();

    }

    private void addressWithEmployeeCountEx6() {
        List<Address> addresses = entityManager
                .createQuery("select a from Address a " +
                        "order by a.employees.size DESC",Address.class)
                .setMaxResults(10)
                .getResultList();


        addresses
                .forEach(address -> {
                    System.out.printf("%s, %s - %d employees%n", address.getText(),
                            address.getTown().getName(),
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
                .setParameter("name",lastName)
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
                        "ORDER BY e.salary, e.id",Employee.class)
                .getResultList();
        for (Employee employee : employees) {
            System.out.printf("%s %s from %s - $%.2f%n",employee.getFirstName(),
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
    }

}
