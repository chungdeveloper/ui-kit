# README #

Backpack is a collection of design resources, reusable components and guidelines for creating VNTravel's products.

### How do I get set up? ###

Backpack is available through Jitpack. To install all of it, add the following line to your build.gradle (in your app module) in the dependencies block:

Go ahead and try:

```
$ implementation 'com.bitbucket.tripi:backpack-android:1.0.0'
```

If your app resolves dependencies through Jitpack you're all set, if not add in your root build.gradle

repositories {
    maven { url 'https://jitpack.io' }
}

Note that Backpack is expected to be used with AndroidX. Please refer to AndroidX migration guide to setup.

### Who do I talk to? ###

* mobile@tripi.vn

### Components ###

* Badge
* Bottom Nav
* Button
* Card
* Calendar
* Checkbox
* Chip
* Dialog
* Horizontal Nav
* Floating Action Button
* Interactive Star Rating
* Nav Bar
* Panel
* Rating
* Snackbar
* Spinner
* Star Rating
* Switch
* Text
* Text Field
* Text Spans
* Toast

### Contribution guidelines - Usage ###

* Writing tests
* Code review
* Other guidelines