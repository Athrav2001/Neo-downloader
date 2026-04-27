#!/usr/bin/env python3
from __future__ import annotations

import argparse
import hashlib
import json
import shutil
from dataclasses import dataclass
from pathlib import Path


REPO = Path("/root/Neo-downloader")
TARGET_ROOT = REPO / "app" / "src" / "main" / "kotlin"
STATE_DIR = REPO / "docs" / "migration"
STATE_FILE = STATE_DIR / "APPLIED_BATCH_STATE.json"


BATCHES: dict[str, list[Path]] = {
    "batch1": [
        REPO / "shared" / "utils",
    ],
    "batch2": [
        REPO / "shared" / "resources",
    ],
    "batch3": [
        REPO / "shared" / "compose-utils",
    ],
    "batch4": [
        REPO / "downloader" / "monitor",
    ],
    "batch5": [
        REPO / "downloader" / "core",
    ],
    "batch6": [
        REPO / "shared" / "updater",
    ],
    "batch7": [
        REPO / "shared" / "auto-start",
    ],
    "batch8": [
        REPO / "shared" / "app",
    ],
}


@dataclass
class CopyResult:
    copied: int = 0
    skipped_same: int = 0
    overwritten: int = 0
    source_files: int = 0


def sha(path: Path) -> str:
    h = hashlib.sha256()
    h.update(path.read_bytes())
    return h.hexdigest()


def iter_source_files(module_root: Path):
    for ss in ("commonMain", "androidMain"):
        base = module_root / "src" / ss / "kotlin"
        if not base.exists():
            continue
        for path in base.rglob("*.kt"):
            if "/build/" in str(path):
                continue
            yield path, base


def load_state() -> dict:
    if STATE_FILE.exists():
        return json.loads(STATE_FILE.read_text(encoding="utf-8"))
    return {"applied_batches": [], "files": {}}


def save_state(state: dict) -> None:
    STATE_DIR.mkdir(parents=True, exist_ok=True)
    STATE_FILE.write_text(json.dumps(state, indent=2, sort_keys=True), encoding="utf-8")


def apply_batch(batch: str, dry_run: bool) -> CopyResult:
    modules = BATCHES[batch]
    result = CopyResult()
    state = load_state()
    state.setdefault("files", {})

    for module in modules:
        for src_file, base in iter_source_files(module):
            rel = src_file.relative_to(base)
            dst = TARGET_ROOT / rel
            result.source_files += 1

            if dst.exists():
                if sha(dst) == sha(src_file):
                    result.skipped_same += 1
                    continue
                result.overwritten += 1
            else:
                result.copied += 1

            if not dry_run:
                dst.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(src_file, dst)
                state["files"][str(dst.relative_to(REPO))] = {
                    "batch": batch,
                    "src": str(src_file.relative_to(REPO)),
                }

    if not dry_run:
        if batch not in state["applied_batches"]:
            state["applied_batches"].append(batch)
        save_state(state)

    return result


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("batch", choices=list(BATCHES.keys()) + ["all"])
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    batches = list(BATCHES.keys()) if args.batch == "all" else [args.batch]
    summary: dict[str, dict] = {}
    for batch in batches:
        result = apply_batch(batch, args.dry_run)
        summary[batch] = {
            "source_files": result.source_files,
            "copied": result.copied,
            "overwritten": result.overwritten,
            "skipped_same": result.skipped_same,
        }

    print(json.dumps(summary, indent=2, sort_keys=True))


if __name__ == "__main__":
    main()
