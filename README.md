# PIZZA RBAC and ABAC

## Into

This is a demo pizza service.
It offers a minimal CRUD APIs to create and retrieve Pizzas and Orders for Pizzas.
Your primary task is to implement role based access control (RBAC) as well as attribute based access control (ABAC).

---

## Task 1 - RBAC

### Task 1.1 - Add RBAC Role

- [] Checkout the branch task-1.1
- [] The enum PizzaRoles defines all known application roles. Add 2 Roles CHEF and CUSTOMER.
- [] Update the PizzaApiAdapter
  - [] A CHEF should be able to read and create pizzas
  - [] A CUSTOMER should only be able to read pizzas
- [] Update the OrdersApiAdapter
  - [] A CHEF should be able to read orders
  - [] A CUSTOMER should be able to create, read, update and delete pizzas
  - [] Hint: You can use Springs @Preautorize with hasRole or hasAnyRole
- [] Make sure all cases RbacTest work.

---

### Task 1.2 - Add RBAC Permissions

Right now we directly check the user roles in the API.
For more complex situations it might be nice to instead have a simple permission like pizza_read that we can validate. 
To support this, we need an additional mapping from the user role to a set of permissions.
One simple way to do this is via resource files.

- [] Create resource files with permissions for each role
- [] Update the SecurityConfig class
  - [] Use the info from the role claim to lookup the relevant file
  - [] Create a GrantedAuthority for each permission and return it in the List
  - [] Make sure, the roles themselves are still returned as well. We will need them for ABAC later.
- [] Update the API Adapters to use hasAuthority instead of hasAnyRole.

---

### Task 1.3 - For the fast ones

- [] Is there a better way of handling permissions? How would you go about externalizing them?
- [] Does the current implementation follow hexagonal architecture principles? Fix if not.
- [] Is the RBAC test a tad over-engineered? How would you simplify it?
- [] Look at https://github.com/jeap-admin-ch/jeap semantic roles
- [] There is quite a bit of functionality missing for a full pizza service. Feel free to implement it.

---
## Part 2 - ABAC for Pizza
---

## Task 2.1 - Create a Salami Pizza ABAC policy

To get an idea of how ABAC could work, we'll implement a simple restriction.

- [] Use AbacTest to debug and verify your solutions. 
- [] If your want you can checkout branch task-2.1. It has all the RBAC tasks implemented. 
- [] Add a new Role SALAMI_ENTHUSIAST
- [] Give it the permission to read pizzas.
- [] Have a look at the AbacPolicy interface
- [] Create a new class AbacOwnOrderPolicy
  - [] Annotate it with @Component so Spring will know it
  - [] Implement the AbacPolicy interface
  - [] Implement the relevantRoles method to return SALAMI_ENTHUSIAST
  - [] Implement the apply method. It should update a JpaQuery<Pizza> to only consider salami Pizzas.

---

## Task 2.2 - Apply the Salami Pizza policy

Now we have a policy. But it isn't applied yet.

- [] Have a look at the classes PizzaPersistenceAdapter and AbacPolicyEngine
- [] Update all find methods in the PizzaPersistenceAdapter so that AbacPolicyEngine is used to apply needed restrictions.
- [] (Optional): AbacPolicyEngine is not particularly well implemented. Can you find a cleaner solution? 

---

## Task 2.2 - Second ABAC rule

It is time for a more complex policy. We want to make sure, that a customer can only read pizzas that are his own.
However, the pizza itself is not directly linked to the user. 
Instead, you'll have to look at the Order table which links both to a pizza and a userId.

- [] Create a new class AbacOwnOrderPolicy
  - [] Annotate it with @Component so Spring will know it
  - [] Implement the AbacPolicy interface
  - [] Implement the relevantRoles method to return CUSTOMER
  - [] Implement the apply method. It should update an JpaQuery<Order> to only consider Orders
     for the current user. You can use SecurityContextAdapter to get the current users id.

---

## Task 2.3 - Update the engine

This is the difficult one.

The AbacPolicyEngine should now automatically apply the new policy.
However, this will not work just yet. 
This is your next task.

- [] Run the tests and have a close look at the error messages
- [] Update the ABAC tooling to only apply the rules for the current entity. 
   There are many ways to do this. Try to find one you are happy with.
- [] If you're stuck, these hints should show you, which solution we chose for this demo:
  - [] Hint1: JpaQuery has a getType Method.
  - [] Hint2: You could use a default method for dispatching.


## Task 3.3 - Combined

Now for the nicer part.
With the framework in place, adding existing restriction to a new role is quite easy.  

- [] Add a new Roles SALAMI_CUSTOMER
- [] Give it the same permissions as a regular customer
- [] Make the needed changes, so that a SALAMI_CUSTOMER can only read pizzas that 
  - [] Are Salami Pizzas
  - [] Have an order assigned


## Task 3.4 - Write Access

So far we have restricted read access. Now let's also look at write and update.

- [] Ensure a user can only update his own Orders and Pizzas
- [] Ensure a user can only create Pizzas and Orders he is allowed to.