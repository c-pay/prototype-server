# cPay

This a Java based backend for cPay online shop.

## Run standalone

Best practice is to run and save any pipes to an log file:

```bash
java -jar prototype.jar >> cpay.log 2>&1
```

## Read logs

Read logs on the fly: 

```bash
tail -f cpay.log
```

## Database

The database behind the web scene in original is MySQL.

## Configuration

You can read, change and setup the credentials or some other configurations here:

`src/main/resources/application.properties`