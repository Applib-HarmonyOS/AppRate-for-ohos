[![.github/workflows/main.yml](https://github.com/applibgroup/AppRate-for-ohos/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/AppRate-for-ohos/actions/workflows/main.yml)  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_AppRate-for-HMOS&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=applibgroup_AppRate-for-HMOS)


# AppRate

A HMOS library which provides AppRate animation

## Source

Inspired by [kikoso/AppRate-for-Android](https://github.com/kikoso/AppRate-for-Android) - version 1.0

## Feature
AppRate-for-HMOS is a library that allows users to rate application in a non intrusive way.
 A prompt with options like Do not ask again, No thanks and Rate is displayed.

<img src="screenshots/Screenshot (2616).png" width="500">


## Dependency
1. For using apprater module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
```groovy
    dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
    implementation project(path: ':app_module')
    testImplementation 'junit:junit:4.13'
    ohosTestImplementation 'com.huawei.ohos.testkit:runner:1.0.0.100'
}
```
2. For using apprater in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```groovy
	dependencies {
		implementation fileTree(dir: 'libs', include: ['*.har'])
		testImplementation 'junit:junit:4.13'
	}
```

## Usage

#### In code
```
super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        new AppRater(this)
                .setMinDays(0)
                .setMinLaunches(0)
                .setAppTitle("My Title")
                .init();

```
