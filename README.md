# 1 - Defining Entities 
### 1.2 - Hibernate entities lifecycle

-  ### Managed Entity
A managed entity is a representation of a database table row (although that row doesn’t have to exist in the database yet).
This is managed by the currently running Session, and every change made on it will be tracked and propagated to the database automatically.
The Session either loads the entity from the database or re-attaches a detached entity. We’ll discuss detached entities in section 5.
Let’s observe some code to get clarification.
Our sample application defines one entity, the FootballPlayer class. At startup, we’ll initialize the data store with some sample data:

| Name              |  ID  | 
|:------------------|:----:|
| Cristiano Ronaldo |  1   |
| Lionel Messi      |  2   |
| Gigi Buffon       |  3   |

Let’s say we want to change the name of Buffon to start with – we want to put in his full name Gianluigi Buffon instead of Gigi Buffon.

First, we need to start our unit of work by obtaining a Session:

``` java
Session session = sessionFactory.openSession();
```

In a server environment, we may inject a Session to our code via a context-aware proxy. The principle remains the same: we need a Session to encapsulate the business transaction of our unit of work.
Next, we’ll instruct our Session to load the data from the persistent store:

``` java
assertThat(getManagedEntities(session)).isEmpty();

List<FootballPlayer> players = s.createQuery("from FootballPlayer").getResultList();

assertThat(getManagedEntities(session)).size().isEqualTo(3);
```
When we first obtain a Session, its persistent context store is empty, as shown by our first assert statement.
Next, we’re executing a query which retrieves data from the database, creates entity representation of the data, and finally returns the entity for us to use.
Internally, the Session keeps track of all entities it loads in the persistent context store. In our case, the Session’s internal store will contain 3 entities after the query.
Now let’s change Gigi’s name:
``` java
Transaction transaction = session.getTransaction();
transaction.begin();

FootballPlayer gigiBuffon = players.stream()
.filter(p -> p.getId() == 3)
.findFirst()
.get();

gigiBuffon.setName("Gianluigi Buffon");
transaction.commit();

assertThat(getDirtyEntities()).size().isEqualTo(1);
assertThat(getDirtyEntities().get(0).getName()).isEqualTo("Gianluigi Buffon");
```
On call to transaction commit() or flush(), the Session will find any dirty entities from its tracking list and synchronize the state to the database.

Notice that we didn’t need to call any method to notify Session that we changed something in our entity – since it’s a managed entity, all changes are propagated to the database automatically.

A managed entity is always a persistent entity – it must have a database identifier, even though the database row representation is not yet created i.e. the INSERT statement is pending the end of the unit of work.

See the chapter about transient entities below.

- ### Detached Entity
  
A detached entity is just an ordinary entity POJO whose identity value corresponds to a database row. The difference from a managed entity is that it’s not tracked anymore by any persistence context.
***An entity can become detached when the Session used to load it was closed, or when we call Session.evict(entity) or Session.clear().***

Let’s see it in the code:

``` java
FootballPlayer cr7 = session.get(FootballPlayer.class, 1L);

assertThat(getManagedEntities(session)).size().isEqualTo(1);
assertThat(getManagedEntities(session).get(0).getId()).isEqualTo(cr7.getId());

session.evict(cr7);

assertThat(getManagedEntities(session)).size().isEqualTo(0);
```
Our persistence context will not track the changes in detached entities:
``` java
cr7.setName("CR7");
transaction.commit();

assertThat(getDirtyEntities()).isEmpty();
```
Session.merge(entity)/Session.update(entity) can (re)attach a session:
``` java
FootballPlayer messi = session.get(FootballPlayer.class, 2L);

session.evict(messi);
messi.setName("Leo Messi");
transaction.commit();

assertThat(getDirtyEntities()).isEmpty();

transaction = startTransaction(session);
session.update(messi);
transaction.commit();

assertThat(getDirtyEntities()).size().isEqualTo(1);
assertThat(getDirtyEntities().get(0).getName()).isEqualTo("Leo Messi");
```

#### The Identity Field Is All That Matters
Let’s have a look at the following logic:
``` java
FootballPlayer gigi = new FootballPlayer();
gigi.setId(3);
gigi.setName("Gigi the Legend");
session.update(gigi);
```
In the example above, we’ve instantiated an entity in the usual way via its constructor. We’ve populated the fields with values and we’ve set the identity to 3, which corresponds to the identity of persistent data that belongs to Gigi Buffon. Calling update() has exactly the same effect as if we’ve loaded the entity from another persistence context.
In fact, Session doesn’t distinguish where a re-attached entity originated from.
It’s quite a common scenario in web applications to construct detached entities from HTML form values.
As far as Session is concerned, a detached entity is just a plain entity whose identity value corresponds to persistent data.
Be aware that the example above just serves a demo purpose. and we need to know exactly what we’re doing. Otherwise, we could end up with null values across our entity if we just set the value on the field we want to update, leaving the rest untouched (so, effectively null).


- ### Transient Entity
A transient entity is simply an entity object that has no representation in the persistent store and is not managed by any Session.
A typical example of a transient entity would be instantiating a new entity via its constructor.
To make a transient entity persistent, we need to call Session.save(entity) or Session.saveOrUpdate(entity):
``` java
FootballPlayer neymar = new FootballPlayer();
neymar.setName("Neymar");
session.save(neymar);

assertThat(getManagedEntities(session)).size().isEqualTo(1);
assertThat(neymar.getId()).isNotNull();

int count = queryCount("select count(*) from Football_Player where name='Neymar'");

assertThat(count).isEqualTo(0);

transaction.commit();
count = queryCount("select count(*) from Football_Player where name='Neymar'");

assertThat(count).isEqualTo(1);
```
As soon as we execute Session.save(entity), the entity is assigned an identity value and becomes managed by the Session. However, it might not yet be available in the database as the INSERT operation might be delayed until the end of the unit of work.
- ### Deleted Entity
An entity is in a deleted (removed) state if Session.delete(entity) has been called, and the Session has marked the entity for deletion. The DELETE command itself might be issued at the end of the unit of work.
Let’s see it in the following code:
``` java
session.delete(neymar);

assertThat(getManagedEntities(session).get(0).getStatus()).isEqualTo(Status.DELETED);
```
However, notice that the entity stays in the persistent context store until the end of the unit of work.