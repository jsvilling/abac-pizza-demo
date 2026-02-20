# PIZZA RBAC and ABAC

## RBAC Role

- Create 3 Roles, CHEF, DELIVERY and CUSTOMER
- All 3 should be able to get PIZZAs
- Only a Chef should be able to create PIZZAS
- Use Springs @Preautorize


## RBAC Permissions

- Externalize the permissions into files
- Use @PreAuthorize with hasAuthority now


## RBAC Permissions JEAP

- If you have time to kill
- Look at https://github.com/jeap-admin-ch/jeap stuff

---



## Salami Chef and Customer

- Add 2 new Roles: SALAMI_CHEF and SALAMI_CUSTOMER
- Make sure they can still do the same actions as customer / chef
- But only allow them to do it for salami pizzas
- Also ensure, getAllPizzas only returns the ones a user may get.

## Delivery

- The Role DELIVERY can get all pizzas right now
- We want to make sure, no one steals pizzas
- So a DELIVERY user should only be able to read pizzas, if they have an Order Entry for this specific pizza
- Consider getAllPizzas again.

## Salami Delivery

- I admit, at this point the whole pizza thing has gone out of hand, and it doesnt make much sense.
- Anyways:
- Add a role Salami Delivery
- This person should only be allowed to deliver salami pizzas but still only see the pizzas assigned to it