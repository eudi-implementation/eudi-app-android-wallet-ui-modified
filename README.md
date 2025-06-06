# EUDI Wallet (modified)

This repository represents a modified version of the [EUDI Wallet Reference Implementation (Android)](https://github.com/eu-digital-identity-wallet/eudi-app-android-wallet-ui).
This README explains the modifications done to get the app running with a locally hosted issuer with self-signed certificates.
For a detailed overview, view the diff to the initial commit (which mirrors the original repo).
Everything else can be found in the [old README](README_old.md).

## Trusting Self-Signed Certificates

The original repository's [How to build](https://github.com/eu-digital-identity-wallet/eudi-app-android-wallet-ui/blob/main/wiki/how_to_build.md) includes a section on how to work with self-signed certificates.
However, the proposed Ktor client is not being used when building the application. This problem is known but wasn't fixed at the time of this project.
Instead, we implement an alternative approach to trust self-signed certificates at the JVM level as explained [here](https://github.com/eu-digital-identity-wallet/eudi-app-android-wallet-ui/pull/299).

We added a new file `core-logic/src/main/java/eu/europa/ec/corelogic/util/SslDevUtility.kt` that contains the necessary changes.
We then modified `core-logic/src/main/java/eu/europa/ec/corelogic/di/LogicCoreModule.kt` to use this new util for "DEBUG" build variants.
Note that this version should never be used for other purposes than development!

## Modifiying Appearance

#### App Appearance

The used color-theme is defined in `resources-logic/src/main/java/eu/europa/ec/resourceslogic/theme/values/ThemeColors.kt`.
If you replace it with your preferred theme, it will be used throughout the app.
However, most drawables in `resources-logic/src/main/res/drawable` have to be adapted manually by simply replacing the colors defined for each path by your preferred color.

To replace the launcher icon for the "dev" build flavor, you can replace the files in `resources-logic/src/dev/res/mipmap-hdpi`, `[...]/mipmap-mdpi`, `[...]/mipmap-xhdpi`, `[...]/mipmap-xxhdpi`, `[...]/mipmap-xxxhdpi`.

To replace the logo that is used throughout the app, add your files to `resources-logic/src/main/res/drawable` and then reference them in `ui-logic/src/main/java/eu/europa/eu/uilogic/component/AppIcons.kt`:
```kotlin
val LogoFull: IconData = IconData(
    resourceId = R.drawable.ic_logo_full, //<----- Change This
    contentDescriptionId = R.string.content_description_logo_full_icon,
    imageVector = null
)

val LogoPlain: IconData = IconData(
    resourceId = R.drawable.ic_logo_plain, //<----- Change This
    contentDescriptionId = R.string.content_description_logo_plain_icon,
    imageVector = null
)

val LogoText: IconData = IconData(
    resourceId = R.drawable.ic_logo_text, //<----- Change This
    contentDescriptionId = R.string.content_description_logo_text_icon,
    imageVector = null
)
```
Make sure that your logos are roughly the same size as the original ones (which shouldn't be a problem if you're using proper svg).

#### Issuer Logo

If you've chosen to change the logo of the [Issuer](https://github.com/eudi-implementation/eudi-srv-web-issuing-eudiw-py-modified), you need to add the generated `my-root-ca.crt` to your devices trust store and then tell this app to use user-installed certificates.
For this, add the following to `network-logic/src/main/res/xml/network_security_config.xml`:
```xml
<trust-anchor>
    <certificates src="system"/>
    <certificates src="user"/>
</trust-anchor>
```
Your IDE probably highlights this as a security issue (rightfully so) as your app will then trust any user-installed certificates.
If you're able to use certificates signed by official CAs, DO NOT add these lines.



