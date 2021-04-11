package com.bacy.seat.util.heimu;

import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.people.Person;
import com.google.gson.JsonObject;

public abstract class AbstractTwoPeopleHeiMu extends AbstractHeiMu{
    protected Person personA;
    protected Person personB;
    public AbstractTwoPeopleHeiMu(int probability, Person personA, Person personB) {
        super(probability);
        this.personA = personA;
        this.personB = personB;
    }
    public AbstractTwoPeopleHeiMu(){
        super();
    }

    @Override
    public boolean isAvailable() {
        return personA!=null&&personB!=null;
    }

    @Override
    public void readData(JsonObject object) {
        super.readData(object);
        personA = PeopleManager.getPerson(object.get("personA").getAsString());
        personB = PeopleManager.getPerson(object.get("personB").getAsString());
    }

    @Override
    public void putToPerson() {
        personA.addHeiMu(this);
        personB.addHeiMu(this);
    }

    public Person getPersonA(){
        return personA;
    }
    public Person getPersonB(){
        return personB;
    }
}
