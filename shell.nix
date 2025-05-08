# shell.nix
{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [
    pkgs.jdk17
    pkgs.jdk21
  ];

  shellHook = ''
    export JAVA_17_HOME="${pkgs.jdk17.home}"
    export JAVA_21_HOME="${pkgs.jdk21.home}"
    export JAVA_HOME="${pkgs.jdk21.home}"

    alias gradle="./gradlew -Porg.gradle.java.installations.fromEnv=JAVA_17_HOME,JAVA_21_HOME"
  '';
}
