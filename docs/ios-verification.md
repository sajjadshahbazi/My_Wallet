# iOS verification list (to check on the Mac)

Add an entry whenever `iosMain`, an iOS `actual`, or Swift code is added/changed.
Format: `- [ ] <date> <module/file>: what to build/run and the expected behavior`

## Always check on the Mac
- [ ] Project builds for iosSimulatorArm64 and iosArm64 (no JVM-only API in `commonMain`).
- [ ] App launches; Compose screens render; previews not needed on iOS.

## Entries
(none yet)
