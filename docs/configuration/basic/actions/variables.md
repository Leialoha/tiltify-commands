Variables
=========

Variables are used in conjunction with commands and messages.

| Variable         | Value                               |
|------------------|-------------------------------------|
| %player          | Name of the donator                 |
| %amount          | Amount from the donation            |
| %totalAmount     | Total amount donated from this user |
| %campaignName    | Campaign name of the donation       |
| %campaignId      | Campaign id of the donation         |
| %campaignOwner   | Campaign owner of the donation      |
| %campaignCurrent | Total donation amount               |
| %campaignGoal    | The goal of the campaign            |
| %campaignCause   | Campaign cause of the donation      |
| %campaignEvent   | Campaign event of the donation      |

Here is an example on how to use a variable:

```
tiltify:
  ...
  actions:
    randomActionName:
      ...
      text:
	    - "Thank you %player for donating!"
	    - "%player donated %amount to %campaignName!"
```