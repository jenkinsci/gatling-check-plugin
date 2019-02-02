# Gatling Check Plugin
A Jenkins plugin that checks Gatling reports against performance metrics.

## Purpose
We want to automate the performing of Gatling load-tests to ensure performance guarantees.
Jenkins is the right tool for scheduling the load-tests, but we still have to check results manually.
This plugin checks gatling reports after simulation executions, and turn build-status to FAILURE if the some performance metrics not met.

## Usage
* This plugin can be used either in Jenkins Web UI or Pipeline (as code). 

### Jenkins Web UI
* For free-style jobs go to **Configuration**, in **Post-build Actions** click **Add post-build action**, then choose **Gatling Checker**.

### Pipeline
* Use `gatlingCheck()` in your jenkins-file to perform check after executing simulation.
* Metrics are specified as a list of strings. 
* Each metric is composed of three parts, namely `<scope>.<type>=<value>`.
* `scope` can be `"global"` or any request name specified in your gatling script.
* The currently supported combinations are list below:

| scope            | type            | value                  |
|------------------|-----------------|------------------------|
| global           | qps             | any positive number    |
| global           | okRate          | any number in [0, 100] |
| global           | responseTime99  | any positive number    |
| global           | responseTime95  | any positive number    |
| global           | responseTimeAvg | any positive number    |
| any request name | qps             | any positive number    |
| any request name | okRate          | any number in [0, 100] |
| any request name | responseTime99  | any positive number    |
| any request name | responseTime95  | any positive number    |
| any request name | responseTimeAvg | any positive number    |

* Example:
```groovy
pipeline {
  agent any
  stages {
    stage('Stress Test') {
      tools {
        maven "maven-3.6.0"
        jdk "jdk-8u202"
      }
      steps {
        sh 'mvn gatling:test -Dgatling.simulationClass=HomeworkListSimulation'
        gatlingCheck(metrics: [
            'global.responseTime99 = 30',
            'get-homework-list-1.qps = 100',
        ])
      }
    }
  }
}
```
* In **Console Output** you will see something like this if metrics are met:
```text
[Gatling Check Plugin] Checking /home/maintain/.jenkins/workspace/Homework-List/tutor-load-test/target/gatling/homeworklistsimulation-20190202094547315/js/stats.json
[Gatling Check Plugin] global .99 response time metric accepted, expected = 30.000000, actual = 23.000000
[Gatling Check Plugin] request get-homework-list-1 qps metric accepted, expected = 100.000000, actual = 125.000000
```
* Or something like this if some metric is not met:
```text
[Gatling Check Plugin] Checking /home/maintain/.jenkins/workspace/Homework-List/tutor-load-test/target/gatling/homeworklistsimulation-20190202103100100/js/stats.json
[Gatling Check Plugin] global .99 response time metric accepted, expected = 30.000000, actual = 23.000000
ERROR: [Gatling Check Plugin] request get-homework-list-1 qps metric unqualified, expected = 100000.000000, actual = 128.205128
Finished: FAILURE
```