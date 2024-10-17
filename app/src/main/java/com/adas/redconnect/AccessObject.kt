package com.adas.redconnect

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessObject {
    private val FirebaseMessgingScope= "https://www.googleapis.com/auth/firebase.messaging"
    fun getAccessToken():String? {
        try{
            val jsonString="{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"red-connect-3994a\",\n" +
                    "  \"private_key_id\": \"047be0f4801934a18cea867b3ca20bfb0d86edc4\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDKXGbiQ3VU8vbu\\n+XAQx3Lb8/yz7ijKLzo7GrdrMHbPaLcOwGP94XnmqkEVd04NXmUk9PBL9FmI5gYb\\nAMexLnbHpZJpyGAogswyGGgKkzU3+dyAPILoXOlN3JqOS1HFOGfxHWA9KQtjCix3\\n6vZWDP8nLrVGEa+1fjw2O2++VZiAFQkFEoRwjAwAS49V7QRqKXb3ZZJ18FH97RH3\\nvBoM39W4l5msx/axd30kXwjl3c+wU0zeNL+3CENghkirEQXwkTNvHLEo+div1XYK\\nuXYIE3vMbpA+NluekGMRGLUzl0H+w0bzQzYDOpTPfaO8JVQyMZqm96u5ofYixa5S\\n/zJwQ13lAgMBAAECggEAFRWooKWvuYCxSvgRfaYSaSmUaHkFpf9ivVrMxq3fiz/Z\\nZv+Ty5WtOmP98Qsjxk/SERY2XWuh0ppJA5JCvErflHDDfyMiJcZvGT8ixAtPjbwK\\nhhAIAccAhhzYu7/MTtTQO7a4WtcGeTTLqlkAfrnO8hmzSJA6MxpqkqoBvB62C441\\n0BJe4e9DLOfiNcRJ1F3pPhPVw2ugEo1mk+Ppr5JIcTeNz0uRux269vtzXA7H/6zK\\nzhUfz2Az59CO/jXNXpXMRuj/3M4xPlP/QMkbqAOSRV11V0NA56rx399WZ4R9JHcy\\nnvuqpP/yQEh4qzego0mj8kmNCCmRAc6b6lu2XboktwKBgQD+RKnsJeZ/t83+FmNd\\nZ6u27xjwBxugIz1n/mAjGc+iWx38zcdhzQDh4RpcG0VEheKlvrhTRZJujxekLHth\\nzbrinT/atvJbh+X5aYPySHoVU9UqrcnIRK4h0xZ+yqueGctjm4NNVbjeNULC7qt8\\nkrrfXahXCBxcIYDtlD8z87a13wKBgQDLvTvaNMjb/8uZK/MDpDuFItuqIKZWtaWr\\nA0tfXAlIgmvowaIpiHbnRACi/MokA41VV+k5Mch8b1dUvkmgmPYv87l0qQZhkAk4\\ney5DNHcuFq+G3ZzDOoxAWWyxO3fKUsN5UIDYlp2decFgBITQQmJ1of4PN3NzOfLx\\nCItLJ9v8uwKBgGCcLEgaunLKns0tKoIB8v+l5z7EJJ365ckOoO6VTwwsPL+iXGBv\\nqScqBX77qZz4lf5N8fFJ3PJ/EgeCEzs9FQYno2xx2LIod5wFmGnFWgWifbsinyml\\n1tfpQYHZdc2zlVD6dLA8oupjKEOytZu2kKRwDJdM8KGc5UTf8AfIUSLXAoGAFEDs\\nmUWUDiLAp6pxzxPdgt+zPJdgUdBe9sAuFv28QoQMnCfbqgtR3q2o7or4wVnFDA3g\\nNnpXdt9OrDL6eMeb+apd7lX7N2mtMPs13yJxEpjYvCx9r/67AI6jOn8x6mAN5Jp3\\nJJDcfFIlQhlQ5q8iwJTo/7RAetzw5Ls3tbHxsY8CgYABs7hDOlFv7Tb9pbz0XdGV\\nOG0PqOhMnHRd4eWBXcf5IEQvg1gULfH5Ba7SGoF9S87+HrAwnEglCT55JvlAxcj6\\nRXCV2QYHG/lsi1WYK6k5FFWN9r05OlmM8jHvCETPa9N4HpPt0YD4u/GFpRD1wOS1\\n8dNvW556HL0xDNhqzQVwSA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-n6rsj@red-connect-3994a.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"104160765464538651689\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-n6rsj%40red-connect-3994a.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"
            val stream= ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials= GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(FirebaseMessgingScope))
            googleCredentials.refresh()

            return googleCredentials.accessToken.tokenValue

        }catch (e: IOException){
            return null
        }
    }

}