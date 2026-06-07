package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.InscriptionDTO;
import com.universite.eventplatform.service.ExportService;
import com.universite.eventplatform.service.InscriptionService;
import com.universite.eventplatform.service.QRCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
@Tag(name = "Inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final ExportService exportService;
    private final QRCodeService qrCodeService;

    @PostMapping("/inscrire")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<InscriptionDTO> inscrire(@RequestParam Long etudiantId, @RequestParam Long eventId) {
        return ResponseEntity.ok(inscriptionService.inscrire(etudiantId, eventId));
    }

    @DeleteMapping("/annuler")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Void> annuler(@RequestParam Long etudiantId, @RequestParam Long eventId) {
        inscriptionService.annuler(etudiantId, eventId); return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<InscriptionDTO> valider(@PathVariable Long id) { return ResponseEntity.ok(inscriptionService.valider(id)); }

    @PatchMapping("/{id}/refuser")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<InscriptionDTO> refuser(@PathVariable Long id) { return ResponseEntity.ok(inscriptionService.refuser(id)); }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<List<InscriptionDTO>> getByEvent(@PathVariable Long eventId) { return ResponseEntity.ok(inscriptionService.getByEvent(eventId)); }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<InscriptionDTO>> getByEtudiant(@PathVariable Long etudiantId) { return ResponseEntity.ok(inscriptionService.getByEtudiant(etudiantId)); }

    @GetMapping("/export/{eventId}/pdf")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long eventId) {
        byte[] data = exportService.exportPdf(eventId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=participants_" + eventId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }

    @GetMapping("/export/{eventId}/excel")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<byte[]> exportExcel(@PathVariable Long eventId) {
        byte[] data = exportService.exportExcel(eventId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=participants_" + eventId + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> getQRCode(@PathVariable Long id) {
        InscriptionDTO ins = inscriptionService.getById(id);
        String qrContent = "Inscription #" + id + " | " + ins.getEtudiantNom()
                + " | " + ins.getEventTitre() + " | " + ins.getStatut();
        byte[] data = qrCodeService.generateQRCode(qrContent, 200, 200);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qrcode_" + id + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(data);
    }
}