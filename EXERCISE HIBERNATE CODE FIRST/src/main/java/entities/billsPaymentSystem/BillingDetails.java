package entities.billsPaymentSystem;

import entities.BaseEntity;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;

@Entity
@Table(name = "billing_details")
public class BillingDetails extends BaseEntity {
    private  String billingDetail;
    private User owner;


    public BillingDetails() {
    }

    @Column(name = "billing_detail")
    public String getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(String billingDetail) {
        this.billingDetail = billingDetail;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
