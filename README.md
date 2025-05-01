# AsmX

![License](https://img.shields.io/badge/License-BSD_3--Clause-blue.svg)
[![Project Status](https://img.shields.io/badge/Status-Active-brightgreen.svg)](https://github.com/OblivRuinDev/AsmX)
![Java Requirement](https://img.shields.io/badge/Java_Requirement-1.8+-red.svg)

![AsmX Logo](.github/avatar.png?raw=true)

---

## 📖 Overview

**ASMX** is an extended bytecode manipulation toolkit based on [ASM](https://asm.ow2.io/) Java bytecode manipulation framework,
designed to address legacy code issues and enhance development flexibility. Key improvements include:

- 🛠️ **Legacy Version Compatibility**: Resolve historical version constraints
- 🧹 **Code Simplification**: Remove redundant version checks
- 🚀 **API Modernization**: Maintain backward compatibility while introducing developer-friendly extensions

Unfortunately, this will break some of the API structure,
but we're still working hard to keep the majority of the API available and add more developer friendly APIs.

For more API breaking changes, please see [changes.md](changes.md)<br>
For actual changes, please see [Compare me](https://github.com/OblivRuinDev/AsmX/compare/upstream...dev)
> ⚠️ **Note**: Require run on Java **1.8+**

> ⚠️ **Note**: ASMX is **not** an official ASM branch. See [Legal Disclaimer](#-legal-disclaimer).

---

## 🔗 Origin Code References

| Source          | Link                                                                                  |
|-----------------|---------------------------------------------------------------------------------------|
| Official ASM    | [OW2 GitLab](https://gitlab.ow2.org/asm/asm)                                          |
| GitHub Sync     | [Unofficial Sync Fork](https://github.com/OblivRuinDev/AsmX/tree/upstream)            |

---

## 🚨 Legal Disclaimer

**ASMX is an independent project with no organizational, financial, or legal ties to:**
- [INRIA](https://www.inria.fr/)
- [France Telecom](https://www.orange.com/)
- OW2 ASM development team

**Copyright Clarification:**
- Modified ASM files carry dual copyright headers (Original ASM + ASMX)
- Newly created files are exclusively under ASMX's BSD-3-Clause
- The "ASM" name remains a trademark of its original rights holders

This project modifies original ASM code under the terms of BSD-3-Clause license. See full [LICENSE](LICENSE).

---

## 🐛 Reporting Issues

If you encounter any issues with the ASMX project, please create a new issue
on the [GitHub Issue Tracker](https://github.com/OblivRuinDev/AsmX/issues).

## 📜 Copyright
### License Hierarchy
| Component Type                    | Rights Holder        | License          |
|-----------------------------------|----------------------|------------------|
| Original ASM Code                 | INRIA/France Telecom | BSD-3-Clause     |
| ASM Modifications and Derivatives | OblivRuinDev         | BSD-3-Clause     |
| New Components                    | OblivRuinDev         | BSD-3-Clause     |

⚠️ **Important Notice**  
This declaration doesn't transfer any original ASM copyrights.  
Full license terms in [LICENSE](LICENSE).
