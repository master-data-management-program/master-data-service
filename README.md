# master-data-service
Maintains the MDM CRUD operations 

#### How to run in Intellij
Execute the 'gradlew clean build bootRun' in Intellij Terminal.

or

Clone the application into local workspace, Open gitbash, Go to Project root folder, execute the "./gradlew clean build bootRun" command.
By default application runs on 2102 port.

#### Sample request for create new table/collection with fields

POST - http://localhost:2102/master/data/management/v1/models?collectionName=<NEW-COLLECTION-NAME>&indexId=<TABLE/COLLECTION-INDEX-ID>
Header - "Content-Type:application/json"


    '''
    {
    "tableName":"<NEW-COLLECTION-NAME>",
    "fields":"{\"assetId\":102,\"name\":\"wheelPart1\",\"address\": \"address\",\"mandatory\":false}",
    "status":"ACTIVE",
    "createdBy":"Admin",
    "createdDateTime":"2019-12-20T19:38:03.180Z[Europe/London]"
    }
    '''
 
##### Note: Still need to agree the contact of input/output data structure. This is initial assumption based on the discussions.
 

##### Technologies Used:

- Spring-Boot
- Lombok
- Spring-boot-starter-data-mongodb
- Embedded MongoDb for Intgration-Testing

