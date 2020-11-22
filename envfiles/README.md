# .env config files for easier local development explained

You can provide custom configurations for your spring boot application.
On your local machine you can set them using a .env file (see _sample.env) which 
would save one from mucking around with ENV parameters

### Creating and using .env files

There is a sample file here called sample.env_ (underscore so its commited to git) this contains a list of env params (without credentials) this project needs, also mentioned in main README.md

* Copy it
* Rename it to lets say dev.env
* Edit it to contain credentials for local development

### Install EnvFile IntelliJ plugin
 
https://github.com/Ashald/EnvFile

* In IntelliJ assuming EnvFile plugin is installed
  * Edit your SpringBoot run profile (call it bench for example)
  * click the EnvFile tab that appears due to plugin
  * tick everything here
  * select the env file you wish to use
  
  
Visual examples given here https://github.com/Ashald/EnvFile

