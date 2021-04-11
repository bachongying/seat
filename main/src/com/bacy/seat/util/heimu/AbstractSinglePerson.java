package com.bacy.seat.util.heimu;

import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.people.Person;
import com.google.gson.JsonObject;

public abstract class AbstractSinglePerson extends AbstractHeiMu{
    protected Person person;
    public AbstractSinglePerson(int probability, Person person) {
        super(probability);
        this.person = person;
    }
    public AbstractSinglePerson(){
        super();
    }

    @Override
    public boolean isAvailable() {
        return person!=null;
    }

    @Override
    public void readData(JsonObject object) {
        super.readData(object);
        person = PeopleManager.getPerson(object.get("person").getAsString());
    }

    @Override
    public void putToPerson() {
        person.addHeiMu(this);
    }
}
