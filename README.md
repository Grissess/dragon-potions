# Dragon Potions

Some simple effects for the [Dragon Survival][ds] dragons.

[ds]: https://github.com/DragonSurvivalTeam/DragonSurvival

## Building

This is currently built against MC 1.20.1; I'll entertain porting it if the
modpack I play ever does so (it shouldn't be hard, though I think DS changed
their age system, so...).

**Note:** `./gradlew runClient` doesn't work yet because I'm too lazy to figure
out proper dev dependencies for DS and GeckoLib. You'll just have to `./gradlew
build` and use the finished `jar` in a modpack proper; that's what I'm doing,
too. It'll be in `build/libs/` once `./gradlew build` finishes.

## Improvements Welcome!

I won't turn down anything that makes this project even slightly more
palatable. Suggestions are also fine, but don't expect me to implement them in
a hurry.

## License

GNU GPL v2. See `COPYING`.
