package com.ilway.skystat.application.usecase;

import com.ilway.skystat.application.dto.management.MetarSaveFileCommand;
import com.ilway.skystat.application.dto.management.MetarSaveFileResult;

public interface MetarSaveFileUseCase {

	MetarSaveFileResult saveFile(MetarSaveFileCommand cmd);

}
