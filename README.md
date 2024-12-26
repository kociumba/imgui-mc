# imgui-mc

ImGui for minecraft based on imgui-java

## This is a fork of the previously maintained fork
This fork exists couse of my need for a few custom features in imgui-mc,
and because the original forks maven repo recently went down.

Because of that, this fork is hosted on github packages,
to avoid this package from ever going down.

## TODO:

- Stop mouse input to imgui while in game
- Wiki
- Add more stuff to this todo list

## Maven repo

### 1.0.3 and below

```groovy
maven {
    name = "breadloaf.public"
    url = "https://maven.breadloaf.xyz/repository/public"
}
```

### 1.0.6 - 1.0.9

```groovy
maven {
    name = "AlignedCookie88"
    url = "https://repo.alignedcookie88.com/repository/maven-public/"
}
```

### 1.0.10 and above

```groovy
// hosting the package on github packages requires authentication
maven {
    url = uri("https://maven.pkg.github.com/kociumba/imgui-mc")
    credentials {
        username = "your github username"
        password = "github access token"
    }
}
```

## dependencies

### 1.0.9 and below

```groovy
modImplementation "xyz.breadloaf.imguimc:imgui-mc:${project.imguimc_version}"
```

## fabric.mod.json

```json
...
"depends": {
...
  "imguimc": "${imguimc_version}",
}
...
```

### 1.0.10 and above

```groovy
// these versions are not going to be published to modrinth so i recommend including in jar
modImplementation(include("xyz.breadloaf.imguimc:imgui-mc:${project.imguimc_version}"))
```

## Current versions

| MC version | imguimc version                                    | imgui-java version                                                     |
|------------|----------------------------------------------------|------------------------------------------------------------------------|
| 1.17.1     | 1.17.1-1.0.3                                       | [1.84.1.1](https://github.com/SpaiR/imgui-java/releases/tag/v1.84.1.1) |
| 1.20.4     | 1.20.4-1.0.4                                       | [1.84.1.1](https://github.com/SpaiR/imgui-java/releases/tag/v1.84.1.1) |
| 1.20.4     | 1.20.4-1.0.5                                       | [1.84.1.1](https://github.com/SpaiR/imgui-java/releases/tag/v1.84.1.1) |
| 1.20.4     | 1.20.4-1.0.6                                       | [1.84.1.1](https://github.com/SpaiR/imgui-java/releases/tag/v1.84.1.1) |
| 1.20.4     | 1.20.4-1.0.7                                       | [1.86.12](https://github.com/SpaiR/imgui-java/releases/tag/1.86.12)    |
| 1.21.1     | 1.21.1-1.0.7                                       | [1.86.12](https://github.com/SpaiR/imgui-java/releases/tag/1.86.12)    |
| 1.21.1     | 1.21.1-1.0.8                                       | [1.86.12](https://github.com/SpaiR/imgui-java/releases/tag/1.86.12)    |
| 1.21.3     | 1.21.3-1.0.9                                       | [1.86.12](https://github.com/SpaiR/imgui-java/releases/tag/1.86.12)    |
| 1.21.1     | 1.21.1-1.0.10 (backport to 1.21.1)                 | 1.86.12                                                                |
| 1.21.1     | 1.21.1-1.0.11 (enables custom font loading)        | 1.86.12                                                                |
| 1.21.1     | 1.21.1-1.0.12 (enables saving the imgui .ini file) | 1.86.12                                                                |

## Features

Use the same familiar imgui interface your used to create a multi window interface in minecraft! (+ screen api for
easier development)

- 1.0.11 - Added custom font loading, through the `imguiInternal.InitCallback` interface.
- 1.0.12 - Added saving the imgui .ini file, provide a custom path by overriding `imguiInternal.ImguiLoader.iniFileName`.

### Extensions:

- ImNodes

  A small, dependency-free node editor for dear imgui. (A good choice for simple start.)
- imgui-node-editor

  Node Editor using ImGui. (A bit more complex than ImNodes, but has more features.)
- ImGuizmo

  Immediate mode 3D gizmo for scene editing and other controls based on Dear Imgui.
- implot

  Advanced 2D Plotting for Dear ImGui.
- ImGuiColorTextEdit

  Syntax highlighting text editor for ImGui.

## Examples

Using ImGuizmo to provide basic 3D tools in game.
![Example showing 3D modeling tools in game](https://i.imgur.com/y65sWyQ.png)

[//]: # (Example showing window dragging)

[//]: # (![GIF showing window dragging]&#40;https://cdn.discordapp.com/attachments/854660703742328884/886957812725452800/Peek_2021-09-13_13-44.gif&#41;)

More examples can be found in my mod utilizing this fork:
[kutils](https://github.com/kociumba/kutils)
